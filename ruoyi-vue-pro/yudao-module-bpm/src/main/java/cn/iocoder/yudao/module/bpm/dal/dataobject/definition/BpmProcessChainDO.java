package cn.iocoder.yudao.module.bpm.dal.dataobject.definition;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.*;

import java.util.List;
import java.util.Map;

/**
 * 串联流程关系 DO
 *
 * @author 芋道源码
 */
@TableName(value = "bpm_process_chain", autoResultMap = true)
@KeySequence("bpm_process_chain_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BpmProcessChainDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;

    /**
     * 串联流程名称
     */
    private String name;

    /**
     * 源流程标识
     */
    private String sourceProcessKey;

    /**
     * 目标流程标识
     */
    private String targetProcessKey;

    /**
     * 源流程名称（非数据库字段，用于显示）
     */
    @TableField(exist = false)
    private String sourceProcessName;

    /**
     * 目标流程名称（非数据库字段，用于显示）
     */
    @TableField(exist = false)
    private String targetProcessName;

    /**
     * 字段映射配置
     * 格式：[
     *   {
     *     "sourceField": "source_field_name",
     *     "targetField": "target_field_name",
     *     "transformRule": "direct|default|expression|wildcard",
     *     "defaultValue": "默认值",
     *     "expression": "表达式"
     *   }
     * ]
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<FieldMapping> fieldMapping;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 备注
     */
    private String remark;

    /**
     * 字段映射配置
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FieldMapping {
        /**
         * 源字段名
         */
        private String sourceField;
        
        /**
         * 目标字段名
         */
        private String targetField;
        
        /**
         * 转换规则：direct-直接复制，default-使用默认值，expression-表达式计算
         */
        private String transformRule;
        
        /**
         * 默认值
         */
        private String defaultValue;
        
        /**
         * 表达式
         */
        private String expression;
    }
} 