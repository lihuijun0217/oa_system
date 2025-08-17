package cn.iocoder.yudao.module.system.controller.admin.regulation.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 规章制度 Response VO")
@Data
public class RegulationRespVO {

    @Schema(description = "规章制度ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "规章制度标题", requiredMode = Schema.RequiredMode.REQUIRED, example = "员工手册")
    private String title;

    @Schema(description = "文件名", requiredMode = Schema.RequiredMode.REQUIRED, example = "员工手册.pdf")
    private String fileName;

    @Schema(description = "文件URL", requiredMode = Schema.RequiredMode.REQUIRED, example = "/uploads/regulations/员工手册.pdf")
    private String fileUrl;

    @Schema(description = "文件大小（字节）", example = "2621440")
    private Long fileSize;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

    @Schema(description = "发布时间")
    private LocalDateTime publishTime;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

} 