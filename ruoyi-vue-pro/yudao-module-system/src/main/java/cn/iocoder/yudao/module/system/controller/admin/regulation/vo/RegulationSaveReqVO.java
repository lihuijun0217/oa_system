package cn.iocoder.yudao.module.system.controller.admin.regulation.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 规章制度新增/修改 Request VO")
@Data
public class RegulationSaveReqVO {

    @Schema(description = "规章制度ID", example = "1024")
    private Long id;

    @Schema(description = "规章制度标题", requiredMode = Schema.RequiredMode.REQUIRED, example = "员工手册")
    @NotEmpty(message = "规章制度标题不能为空")
    private String title;

    @Schema(description = "文件名", requiredMode = Schema.RequiredMode.REQUIRED, example = "员工手册.pdf")
    @NotEmpty(message = "文件名不能为空")
    private String fileName;

    @Schema(description = "文件URL", requiredMode = Schema.RequiredMode.REQUIRED, example = "/uploads/regulations/员工手册.pdf")
    @NotEmpty(message = "文件URL不能为空")
    private String fileUrl;

    @Schema(description = "文件大小（字节）", example = "2621440")
    private Long fileSize;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "状态不能为空")
    private Integer status;

    @Schema(description = "发布时间")
    private LocalDateTime publishTime;

} 