package cn.iocoder.yudao.module.bpm.controller.admin.definition.vo.chain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "管理后台 - 串联流程关系 Response VO")
@Data
public class BpmProcessChainRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "串联流程名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "采购物资申请->资产入库申请")
    private String name;

    @Schema(description = "源流程标识", requiredMode = Schema.RequiredMode.REQUIRED, example = "cg_wzsq")
    private String sourceProcessKey;

    @Schema(description = "目标流程标识", requiredMode = Schema.RequiredMode.REQUIRED, example = "cg_zcrk")
    private String targetProcessKey;

    @Schema(description = "字段映射配置")
    private List<FieldMappingRespVO> fieldMapping;

    @Schema(description = "是否启用", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    private Boolean enabled;

    @Schema(description = "排序", example = "1")
    private Integer sort;

    @Schema(description = "备注", example = "采购物资申请审批通过后，可以发起资产入库申请")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    @Schema(description = "源流程名称", example = "采购物资申请")
    private String sourceProcessName;

    @Schema(description = "目标流程名称", example = "资产入库申请")
    private String targetProcessName;

    @Schema(description = "字段映射配置")
    @Data
    public static class FieldMappingRespVO {

        @Schema(description = "源字段名", example = "purchaseQuantity")
        private String sourceField;

        @Schema(description = "目标字段名", example = "inboundQuantity")
        private String targetField;

        @Schema(description = "转换规则", example = "direct")
        private String transformRule;

        @Schema(description = "默认值", example = "0")
        private String defaultValue;

        @Schema(description = "表达式", example = "#{sourceValue * 1.1}")
        private String expression;

        @Schema(description = "源字段标签", example = "采购数量")
        private String sourceFieldLabel;

        @Schema(description = "目标字段标签", example = "入库数量")
        private String targetFieldLabel;
    }

} 