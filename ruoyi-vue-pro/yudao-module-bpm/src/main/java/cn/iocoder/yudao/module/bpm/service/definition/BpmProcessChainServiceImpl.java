package cn.iocoder.yudao.module.bpm.service.definition;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.bpm.controller.admin.definition.vo.chain.*;
import cn.iocoder.yudao.module.bpm.convert.definition.BpmProcessChainConvert;
import cn.iocoder.yudao.module.bpm.dal.dataobject.definition.BpmProcessChainDO;
import cn.iocoder.yudao.module.bpm.dal.mysql.definition.BpmProcessChainMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.HashSet;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception0;

/**
 * 串联流程关系 Service 实现类
 *
 * @author 芋道源码
 */
@Slf4j
@Service
@Validated
public class BpmProcessChainServiceImpl implements BpmProcessChainService {

    @Resource
    private BpmProcessChainMapper processChainMapper;

    @Resource
    private BpmProcessDefinitionService processDefinitionService;

    @Override
    public Long createProcessChain(BpmProcessChainCreateReqVO createReqVO) {
        // 1. 校验流程关系是否已存在
        validateProcessChainExists(createReqVO.getSourceProcessKey(), createReqVO.getTargetProcessKey());
        
        // 2. 插入
        BpmProcessChainDO processChain = BpmProcessChainConvert.INSTANCE.convert(createReqVO);
        processChainMapper.insert(processChain);
        return processChain.getId();
    }

    @Override
    public void updateProcessChain(BpmProcessChainUpdateReqVO updateReqVO) {
        // 1. 校验存在
        BpmProcessChainDO processChain = validateProcessChainExists(updateReqVO.getId());
        
        // 2. 校验流程关系是否已存在（排除自己）
        if (!processChain.getSourceProcessKey().equals(updateReqVO.getSourceProcessKey()) ||
            !processChain.getTargetProcessKey().equals(updateReqVO.getTargetProcessKey())) {
            validateProcessChainExists(updateReqVO.getSourceProcessKey(), updateReqVO.getTargetProcessKey());
        }
        
        // 3. 更新
        BpmProcessChainDO updateObj = BpmProcessChainConvert.INSTANCE.convert(updateReqVO);
        processChainMapper.updateById(updateObj);
    }

    @Override
    public void deleteProcessChain(Long id) {
        // 1. 校验存在
        validateProcessChainExists(id);
        
        // 2. 删除
        processChainMapper.deleteById(id);
    }

    @Override
    public BpmProcessChainDO getProcessChain(Long id) {
        return processChainMapper.selectById(id);
    }

    @Override
    public PageResult<BpmProcessChainDO> getProcessChainPage(BpmProcessChainPageReqVO pageReqVO) {
        // 构建查询条件
        PageResult<BpmProcessChainDO> pageResult = processChainMapper.selectPage(pageReqVO, new LambdaQueryWrapperX<BpmProcessChainDO>()
                .likeIfPresent(BpmProcessChainDO::getName, pageReqVO.getName())
                .eqIfPresent(BpmProcessChainDO::getSourceProcessKey, pageReqVO.getSourceProcessKey())
                .eqIfPresent(BpmProcessChainDO::getTargetProcessKey, pageReqVO.getTargetProcessKey())
                .eqIfPresent(BpmProcessChainDO::getEnabled, pageReqVO.getEnabled())
                .betweenIfPresent(BpmProcessChainDO::getCreateTime, pageReqVO.getCreateTime())
                .orderByDesc(BpmProcessChainDO::getId));
        
        // 填充流程定义名称
        if (CollUtil.isNotEmpty(pageResult.getList())) {
            fillProcessDefinitionNames(pageResult.getList());
        }
        
        return pageResult;
    }

    @Override
    public List<BpmProcessChainDO> getProcessChainsBySourceKey(String sourceProcessKey) {
        if (StrUtil.isEmpty(sourceProcessKey)) {
            return new ArrayList<>();
        }
        List<BpmProcessChainDO> chains = processChainMapper.selectBySourceProcessKey(sourceProcessKey);
        if (CollUtil.isNotEmpty(chains)) {
            fillProcessDefinitionNames(chains);
        }
        return chains;
    }

    @Override
    public List<BpmProcessChainDO> getProcessChainsByTargetKey(String targetProcessKey) {
        if (StrUtil.isEmpty(targetProcessKey)) {
            return new ArrayList<>();
        }
        List<BpmProcessChainDO> chains = processChainMapper.selectByTargetProcessKey(targetProcessKey);
        if (CollUtil.isNotEmpty(chains)) {
            fillProcessDefinitionNames(chains);
        }
        return chains;
    }

    @Override
    public Map<String, Object> generateTargetProcessVariables(String sourceProcessKey, String targetProcessKey, Map<String, Object> sourceVariables) {
        log.info("生成目标流程变量 - 源流程: {}, 目标流程: {}, 源变量: {}", sourceProcessKey, targetProcessKey, sourceVariables);
        
        // 测试通配符匹配逻辑
        testWildcardMatching();
        
        // 1. 查询字段映射配置
        List<BpmProcessChainDO> processChains = processChainMapper.selectBySourceProcessKey(sourceProcessKey);
        log.info("查询到的流程串联配置数量: {}", processChains.size());
        
        BpmProcessChainDO processChain = processChains.stream()
                .filter(chain -> targetProcessKey.equals(chain.getTargetProcessKey()))
                .findFirst()
                .orElse(null);
        
        if (processChain == null) {
            log.warn("未找到源流程 {} 到目标流程 {} 的串联配置", sourceProcessKey, targetProcessKey);
            return new HashMap<>();
        }
        
        if (CollUtil.isEmpty(processChain.getFieldMapping())) {
            log.warn("流程串联配置 {} 没有字段映射", processChain.getId());
            return new HashMap<>();
        }

        log.info("找到流程串联配置: {}, 字段映射数量: {}", processChain.getId(), processChain.getFieldMapping().size());

        // 2. 根据字段映射配置生成目标流程变量
        Map<String, Object> targetVariables = new HashMap<>();
        for (BpmProcessChainDO.FieldMapping fieldMapping : processChain.getFieldMapping()) {
            log.info("处理字段映射: {} -> {}, 规则: {}", fieldMapping.getSourceField(), fieldMapping.getTargetField(), fieldMapping.getTransformRule());
            
            if ("wildcard".equals(fieldMapping.getTransformRule())) {
                // 处理通配符映射
                processWildcardMapping(fieldMapping, sourceVariables, targetVariables);
            } else {
                // 处理普通字段映射
                processFieldMapping(fieldMapping, sourceVariables, targetVariables);
            }
        }

        log.info("生成的目标流程变量: {}", targetVariables);
        return targetVariables;
    }

    /**
     * 处理通配符映射
     */
    private void processWildcardMapping(BpmProcessChainDO.FieldMapping mapping, Map<String, Object> sourceVariables, Map<String, Object> targetVariables) {
        String sourcePattern = mapping.getSourceField();
        String targetPattern = mapping.getTargetField();
        
        log.info("处理通配符映射 - 源模式: {}, 目标模式: {}", sourcePattern, targetPattern);
        
        if (!sourcePattern.contains("*") && !targetPattern.contains("*")) {
            // 如果没有通配符，按普通字段处理
            log.info("模式中没有通配符，按普通字段处理");
            processFieldMapping(mapping, sourceVariables, targetVariables);
            return;
        }

        // 将通配符模式转换为正则表达式
        String sourceRegex = convertWildcardToRegex(sourcePattern);
        
        log.info("开始遍历源变量，查找匹配的字段，正则表达式: {}", sourceRegex);
        
        // 遍历源变量，查找匹配的字段
        for (Map.Entry<String, Object> entry : sourceVariables.entrySet()) {
            String sourceField = entry.getKey();
            Object sourceValue = entry.getValue();
            
            log.info("检查字段: {} 是否匹配正则表达式: {}", sourceField, sourceRegex);
            
            if (sourceField.matches(sourceRegex)) {
                log.info("字段 {} 匹配成功", sourceField);
                // 生成目标字段名
                String targetField = generateTargetFieldName(sourceField, sourcePattern, targetPattern);
                if (targetField != null) {
                    targetVariables.put(targetField, sourceValue);
                    log.info("通配符映射成功: {} -> {} = {}", sourceField, targetField, sourceValue);
                } else {
                    log.warn("无法生成目标字段名: {} -> {}", sourceField, targetPattern);
                }
            } else {
                log.info("字段 {} 不匹配正则表达式", sourceField);
            }
        }
    }

    /**
     * 将通配符模式转换为正则表达式
     */
    private String convertWildcardToRegex(String pattern) {
        if (pattern == null) return "";
        
        log.info("转换通配符模式为正则表达式: {}", pattern);
        
        // 对于简单的通配符模式，直接替换*为.*
        // 如果模式中包含其他正则表达式特殊字符，再进行转义
        String regex = pattern.replace("*", ".*");
        
        // 检查是否包含需要转义的特殊字符
//        if (regex.contains(".") || regex.contains("^") || regex.contains("$") ||
//            regex.contains("+") || regex.contains("?") || regex.contains("(") ||
//            regex.contains(")") || regex.contains("[") || regex.contains("]") ||
//            regex.contains("{") || regex.contains("}") || regex.contains("|") ||
//            regex.contains("\\")) {
//
//            log.info("检测到特殊字符，进行转义处理");
//            regex = regex.replaceAll("([.^$+?()\\[\\]{}|\\\\])", "\\\\$1");
//        }
        
        log.info("最终转换后的正则表达式: {}", regex);
        
        return regex;
    }

    /**
     * 根据源字段名和模式生成目标字段名
     */
    private String generateTargetFieldName(String sourceField, String sourcePattern, String targetPattern) {
        log.info("生成目标字段名 - 源字段: {}, 源模式: {}, 目标模式: {}", sourceField, sourcePattern, targetPattern);
        
        if (!sourcePattern.contains("*")) {
            log.info("源模式中没有通配符，直接返回目标模式: {}", targetPattern);
            return targetPattern;
        }
        
        // 找到通配符的位置
        int wildcardIndex = sourcePattern.indexOf('*');
        if (wildcardIndex == -1) {
            log.info("源模式中没有找到通配符，直接返回目标模式: {}", targetPattern);
            return targetPattern;
        }
        
        // 提取通配符前的部分
        String beforeWildcard = sourcePattern.substring(0, wildcardIndex);
        // 提取通配符后的部分
        String afterWildcard = sourcePattern.substring(wildcardIndex + 1);
        
        log.info("通配符分析 - 通配符位置: {}, 通配符前: '{}', 通配符后: '{}'", wildcardIndex, beforeWildcard, afterWildcard);
        
        // 从源字段中提取通配符对应的部分
        if (sourceField.startsWith(beforeWildcard) && sourceField.endsWith(afterWildcard)) {
            String wildcardPart = sourceField.substring(beforeWildcard.length(), 
                sourceField.length() - afterWildcard.length());
            
            log.info("提取的通配符部分: '{}'", wildcardPart);
            
            // 在目标模式中替换通配符
            String result = targetPattern.replace("*", wildcardPart);
            log.info("生成的目标字段名: {}", result);
            return result;
        } else {
            log.warn("源字段 '{}' 不匹配模式 '{}' (通配符前: '{}', 通配符后: '{}')", 
                    sourceField, sourcePattern, beforeWildcard, afterWildcard);
        }
        
        return null;
    }

    /**
     * 处理普通字段映射
     */
    private void processFieldMapping(BpmProcessChainDO.FieldMapping mapping, Map<String, Object> sourceVariables, Map<String, Object> targetVariables) {
        String sourceField = mapping.getSourceField();
        String targetField = mapping.getTargetField();
        String transformRule = mapping.getTransformRule();
        
        Object sourceValue = sourceVariables.get(sourceField);
        log.info("字段映射处理 - 源字段: {}, 目标字段: {}, 规则: {}, 源值: {}", sourceField, targetField, transformRule, sourceValue);
        
        switch (transformRule) {
            case "direct":
                // 直接复制
                if (sourceValue != null) {
                    targetVariables.put(targetField, sourceValue);
                    log.info("直接复制: {} -> {} = {}", sourceField, targetField, sourceValue);
                } else {
                    log.warn("源字段 {} 的值为空，跳过复制", sourceField);
                }
                break;
            case "default":
                // 使用默认值
                Object value = sourceValue != null ? sourceValue : mapping.getDefaultValue();
                targetVariables.put(targetField, value);
                log.info("使用默认值: {} -> {} = {} (默认值: {})", sourceField, targetField, value, mapping.getDefaultValue());
                break;
            case "expression":
                // 表达式计算
                if (StrUtil.isNotBlank(mapping.getExpression())) {
                    Object result = evaluateExpression(mapping.getExpression(), sourceVariables);
                    targetVariables.put(targetField, result);
                    log.info("表达式计算: {} -> {} = {} (表达式: {})", sourceField, targetField, result, mapping.getExpression());
                } else {
                    log.warn("表达式为空，跳过计算");
                }
                break;
        }
    }

    private Object evaluateExpression(String expression, Map<String, Object> sourceVariables) {
        // 这里简化处理，实际可以集成SpEL表达式引擎
        // 例如：#{sourceValue * 1.1} 可以解析并计算
        if (StrUtil.isEmpty(expression)) {
            return null;
        }
        
        // 简单的变量替换，实际项目中建议使用SpEL
        for (Map.Entry<String, Object> entry : sourceVariables.entrySet()) {
            expression = expression.replace("#{" + entry.getKey() + "}", String.valueOf(entry.getValue()));
        }
        
        return expression;
    }

    private BpmProcessChainDO validateProcessChainExists(Long id) {
        BpmProcessChainDO processChain = processChainMapper.selectById(id);
        if (processChain == null) {
            throw exception0(1001001, "串联流程关系不存在");
        }
        return processChain;
    }

    private void validateProcessChainExists(String sourceProcessKey, String targetProcessKey) {
        List<BpmProcessChainDO> existingChains = processChainMapper.selectBySourceProcessKey(sourceProcessKey);
        boolean exists = existingChains.stream()
                .anyMatch(chain -> targetProcessKey.equals(chain.getTargetProcessKey()));
        if (exists) {
            throw exception0(1001002, "串联流程关系已存在");
        }
    }

    /**
     * 填充流程定义名称
     */
    private void fillProcessDefinitionNames(List<BpmProcessChainDO> processChains) {
        // 收集所有流程定义key
        Set<String> processKeys = new HashSet<>();
        for (BpmProcessChainDO chain : processChains) {
            processKeys.add(chain.getSourceProcessKey());
            processKeys.add(chain.getTargetProcessKey());
        }
        
        // 获取流程定义信息
        Map<String, String> processNameMap = getProcessDefinitionNames(processKeys);
        
        // 填充名称
        for (BpmProcessChainDO chain : processChains) {
            chain.setSourceProcessName(processNameMap.get(chain.getSourceProcessKey()));
            chain.setTargetProcessName(processNameMap.get(chain.getTargetProcessKey()));
        }
    }

    /**
     * 获取流程定义名称映射
     */
    private Map<String, String> getProcessDefinitionNames(Set<String> processKeys) {
        Map<String, String> nameMap = new HashMap<>();
        if (CollUtil.isEmpty(processKeys)) {
            return nameMap;
        }
        
        try {
            // 使用流程定义服务获取名称
            for (String processKey : processKeys) {
                try {
                    // 获取流程定义信息
                    var processDefinition = processDefinitionService.getActiveProcessDefinition(processKey);
                    if (processDefinition != null) {
                        nameMap.put(processKey, processDefinition.getName());
                    } else {
                        nameMap.put(processKey, processKey);
                    }
                } catch (Exception e) {
                    // 如果获取失败，使用key作为名称
                    nameMap.put(processKey, processKey);
                }
            }
        } catch (Exception e) {
            // 如果获取失败，使用key作为名称
            for (String processKey : processKeys) {
                nameMap.put(processKey, processKey);
            }
        }
        
        return nameMap;
    }

    /**
     * 测试通配符匹配逻辑
     */
    public void testWildcardMatching() {
        log.info("=== 开始测试通配符匹配逻辑 ===");
        
        // 测试用例1: lb_* -> lb_*
        String pattern1 = "lb_*";
        String field1 = "lb_xh_1";
        String regex1 = convertWildcardToRegex(pattern1);
        boolean match1 = field1.matches(regex1);
        log.info("测试1 - 模式: {}, 字段: {}, 正则: {}, 匹配: {}", pattern1, field1, regex1, match1);
        
        // 测试用例2: lb_* -> lb_xh_2 (您日志中的实际字段)
        String field2 = "lb_xh_2";
        boolean match2 = field2.matches(regex1);
        log.info("测试2 - 模式: {}, 字段: {}, 正则: {}, 匹配: {}", pattern1, field2, regex1, match2);
        
        // 测试用例3: 手动验证正则表达式
        String manualRegex = "lb_.*";
        boolean manualMatch1 = field1.matches(manualRegex);
        boolean manualMatch2 = field2.matches(manualRegex);
        log.info("手动测试 - 正则: {}, 字段1: {}, 匹配1: {}, 字段2: {}, 匹配2: {}", 
                manualRegex, field1, manualMatch1, field2, manualMatch2);
        
        // 测试用例4: *_name -> *_name
        String pattern4 = "*_name";
        String field4 = "user_name";
        String regex4 = convertWildcardToRegex(pattern4);
        boolean match4 = field4.matches(regex4);
        log.info("测试4 - 模式: {}, 字段: {}, 正则: {}, 匹配: {}", pattern4, field4, regex4, match4);
        
        // 测试用例5: user_*_info -> user_*_info
        String pattern5 = "user_*_info";
        String field5 = "user_profile_info";
        String regex5 = convertWildcardToRegex(pattern5);
        boolean match5 = field5.matches(regex5);
        log.info("测试5 - 模式: {}, 字段: {}, 正则: {}, 匹配: {}", pattern5, field5, regex5, match5);
        
        log.info("=== 通配符匹配逻辑测试完成 ===");
    }

} 