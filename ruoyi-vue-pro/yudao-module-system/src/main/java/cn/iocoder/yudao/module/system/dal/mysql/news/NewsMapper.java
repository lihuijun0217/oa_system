package cn.iocoder.yudao.module.system.dal.mysql.news;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.system.controller.admin.news.vo.NewsPageReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.news.NewsDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 新闻 Mapper
 *
 * @author ruoyi
 */
@Mapper
public interface NewsMapper extends BaseMapperX<NewsDO> {

    default PageResult<NewsDO> selectPage(NewsPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<NewsDO>()
                .likeIfPresent(NewsDO::getTitle, reqVO.getTitle())
                .eqIfPresent(NewsDO::getType, reqVO.getType())
                .eqIfPresent(NewsDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(NewsDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(NewsDO::getId));
    }

} 