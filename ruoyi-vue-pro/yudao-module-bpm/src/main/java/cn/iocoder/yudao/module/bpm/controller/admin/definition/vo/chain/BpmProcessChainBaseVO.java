package cn.iocoder.yudao.module.bpm.controller.admin.definition.vo.chain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Schema(description = "管理后台 - 串联流程关系 Base VO")
@Data
public class BpmProcessChainBaseVO {

    @Schema(description = "串联流程名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "采购物资申请->资产入库申请")
    @NotEmpty(message = "串联流程名称不能为空")
    private String name;

    @Schema(description = "源流程标识", requiredMode = Schema.RequiredMode.REQUIRED, example = "cg_wzsq")
    @NotEmpty(message = "源流程标识不能为空")
    private String sourceProcessKey;

    @Schema(description = "目标流程标识", requiredMode = Schema.RequiredMode.REQUIRED, example = "cg_zcrk")
    @NotEmpty(message = "目标流程标识不能为空")
    private String targetProcessKey;

    @Schema(description = "字段映射配置")
    private List<FieldMappingVO> fieldMapping;

    @Schema(description = "是否启用", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    @NotNull(message = "是否启用不能为空")
    private Boolean enabled;

    @Schema(description = "排序", example = "1")
    private Integer sort;

    @Schema(description = "备注", example = "采购物资申请审批通过后，可以发起资产入库申请")
    private String remark;

    @Schema(description = "字段映射配置")
    @Data
    public static class FieldMappingVO {

        @Schema(description = "源字段名", requiredMode = Schema.RequiredMode.REQUIRED, example = "purchaseQuantity")
        @NotEmpty(message = "源字段名不能为空")
        private String sourceField;

        @Schema(description = "目标字段名", requiredMode = Schema.RequiredMode.REQUIRED, example = "inboundQuantity")
        @NotEmpty(message = "目标字段名不能为空")
        private String targetField;

        @Schema(description = "转换规则", requiredMode = Schema.RequiredMode.REQUIRED, example = "direct", allowableValues = {"direct", "default", "expression"})
        @NotEmpty(message = "转换规则不能为空")
        private String transformRule;

        @Schema(description = "默认值", example = "0")
        private String defaultValue;

        @Schema(description = "表达式", example = "#{sourceValue * 1.1}")
        private String expression;
    }
} 