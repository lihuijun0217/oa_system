package cn.iocoder.yudao.module.bpm.controller.admin.definition.vo.chain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

@Schema(description = "管理后台 - 串联流程生成变量 Request VO")
@Data
public class BpmProcessChainGenerateVariablesReqVO {

    @Schema(description = "源流程标识", requiredMode = Schema.RequiredMode.REQUIRED, example = "cg_wzsq")
    @NotBlank(message = "源流程标识不能为空")
    private String sourceProcessKey;

    @Schema(description = "目标流程标识", requiredMode = Schema.RequiredMode.REQUIRED, example = "cg_zcrk")
    @NotBlank(message = "目标流程标识不能为空")
    private String targetProcessKey;

    @Schema(description = "源流程变量", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "源流程变量不能为空")
    private Map<String, Object> sourceVariables;

} 