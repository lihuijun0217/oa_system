package cn.iocoder.yudao.module.bpm.service.definition;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.bpm.controller.admin.definition.vo.chain.*;
import cn.iocoder.yudao.module.bpm.dal.dataobject.definition.BpmProcessChainDO;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 串联流程关系 Service 接口
 *
 * @author 芋道源码
 */
public interface BpmProcessChainService {

    /**
     * 创建串联流程关系
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createProcessChain(@Valid BpmProcessChainCreateReqVO createReqVO);

    /**
     * 更新串联流程关系
     *
     * @param updateReqVO 更新信息
     */
    void updateProcessChain(@Valid BpmProcessChainUpdateReqVO updateReqVO);

    /**
     * 删除串联流程关系
     *
     * @param id 编号
     */
    void deleteProcessChain(Long id);

    /**
     * 获得串联流程关系
     *
     * @param id 编号
     * @return 串联流程关系
     */
    BpmProcessChainDO getProcessChain(Long id);

    /**
     * 获得串联流程关系分页
     *
     * @param pageReqVO 分页查询
     * @return 串联流程关系分页
     */
    PageResult<BpmProcessChainDO> getProcessChainPage(BpmProcessChainPageReqVO pageReqVO);

    /**
     * 根据源流程标识查询可发起的后续流程
     *
     * @param sourceProcessKey 源流程标识
     * @return 串联流程关系列表
     */
    List<BpmProcessChainDO> getProcessChainsBySourceKey(String sourceProcessKey);

    /**
     * 根据目标流程标识查询前置流程
     *
     * @param targetProcessKey 目标流程标识
     * @return 串联流程关系列表
     */
    List<BpmProcessChainDO> getProcessChainsByTargetKey(String targetProcessKey);

    /**
     * 基于前流程数据，生成目标流程的变量
     *
     * @param sourceProcessKey 源流程标识
     * @param targetProcessKey 目标流程标识
     * @param sourceVariables 源流程变量
     * @return 目标流程变量
     */
    Map<String, Object> generateTargetProcessVariables(String sourceProcessKey, String targetProcessKey, Map<String, Object> sourceVariables);

} 