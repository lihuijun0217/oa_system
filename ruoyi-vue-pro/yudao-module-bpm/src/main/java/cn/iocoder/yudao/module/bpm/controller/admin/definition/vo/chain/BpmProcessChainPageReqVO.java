package cn.iocoder.yudao.module.bpm.controller.admin.definition.vo.chain;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 串联流程关系分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BpmProcessChainPageReqVO extends PageParam {

    @Schema(description = "串联流程名称", example = "采购物资申请->资产入库申请")
    private String name;

    @Schema(description = "源流程标识", example = "cg_wzsq")
    private String sourceProcessKey;

    @Schema(description = "目标流程标识", example = "cg_zcrk")
    private String targetProcessKey;

    @Schema(description = "是否启用", example = "true")
    private Boolean enabled;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

} 