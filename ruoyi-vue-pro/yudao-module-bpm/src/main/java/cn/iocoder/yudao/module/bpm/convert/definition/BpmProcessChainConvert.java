package cn.iocoder.yudao.module.bpm.convert.definition;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.bpm.controller.admin.definition.vo.chain.*;
import cn.iocoder.yudao.module.bpm.dal.dataobject.definition.BpmProcessChainDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 串联流程关系 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface BpmProcessChainConvert {

    BpmProcessChainConvert INSTANCE = Mappers.getMapper(BpmProcessChainConvert.class);

    BpmProcessChainDO convert(BpmProcessChainCreateReqVO bean);

    BpmProcessChainDO convert(BpmProcessChainUpdateReqVO bean);

    BpmProcessChainRespVO convert(BpmProcessChainDO bean);

    List<BpmProcessChainRespVO> convertList(List<BpmProcessChainDO> list);

    PageResult<BpmProcessChainRespVO> convertPage(PageResult<BpmProcessChainDO> page);

} 