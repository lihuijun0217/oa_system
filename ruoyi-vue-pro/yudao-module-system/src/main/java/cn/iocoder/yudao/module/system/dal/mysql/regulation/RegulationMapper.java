package cn.iocoder.yudao.module.system.dal.mysql.regulation;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.system.controller.admin.regulation.vo.RegulationPageReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.regulation.RegulationDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 规章制度 Mapper
 *
 * @author ruoyi
 */
@Mapper
public interface RegulationMapper extends BaseMapperX<RegulationDO> {

    default PageResult<RegulationDO> selectPage(RegulationPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<RegulationDO>()
                .likeIfPresent(RegulationDO::getTitle, reqVO.getTitle())
                .eqIfPresent(RegulationDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(RegulationDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(RegulationDO::getId));
    }

} 