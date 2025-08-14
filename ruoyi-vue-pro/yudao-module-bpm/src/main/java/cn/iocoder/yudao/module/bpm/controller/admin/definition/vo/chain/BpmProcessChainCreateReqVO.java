package cn.iocoder.yudao.module.bpm.controller.admin.definition.vo.chain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 串联流程关系创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BpmProcessChainCreateReqVO extends BpmProcessChainBaseVO {

} 