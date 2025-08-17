package cn.iocoder.yudao.module.system.service.news;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.system.controller.admin.news.vo.NewsPageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.news.vo.NewsSaveReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.news.NewsDO;
import cn.iocoder.yudao.module.system.dal.mysql.news.NewsMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.NEWS_NOT_FOUND;

/**
 * 新闻 Service 实现类
 *
 * @author ruoyi
 */
@Service
@Validated
public class NewsServiceImpl implements NewsService {

    @Resource
    private NewsMapper newsMapper;

    @Override
    public Long createNews(NewsSaveReqVO createReqVO) {
        // 插入
        NewsDO news = BeanUtils.toBean(createReqVO, NewsDO.class);
        // 如果状态是已发布，设置发布时间
        if (createReqVO.getStatus() != null && createReqVO.getStatus() == 1) {
            news.setPublishTime(LocalDateTime.now());
        }
        newsMapper.insert(news);
        // 返回
        return news.getId();
    }

    @Override
    public void updateNews(NewsSaveReqVO updateReqVO) {
        // 校验存在
        validateNewsExists(updateReqVO.getId());
        // 更新
        NewsDO updateObj = BeanUtils.toBean(updateReqVO, NewsDO.class);
        // 如果状态是已发布，设置发布时间
        if (updateReqVO.getStatus() != null && updateReqVO.getStatus() == 1) {
            updateObj.setPublishTime(LocalDateTime.now());
        }
        newsMapper.updateById(updateObj);
    }

    @Override
    public void deleteNews(Long id) {
        // 校验存在
        validateNewsExists(id);
        // 删除
        newsMapper.deleteById(id);
    }

    private void validateNewsExists(Long id) {
        if (newsMapper.selectById(id) == null) {
            throw exception(NEWS_NOT_FOUND);
        }
    }

    @Override
    public NewsDO getNews(Long id) {
        return newsMapper.selectById(id);
    }

    @Override
    public PageResult<NewsDO> getNewsPage(NewsPageReqVO pageReqVO) {
        return newsMapper.selectPage(pageReqVO);
    }

    @Override
    public void publishNews(Long id) {
        // 校验存在
        validateNewsExists(id);
        // 更新状态为已发布
        NewsDO updateObj = new NewsDO();
        updateObj.setId(id);
        updateObj.setStatus(1);
        updateObj.setPublishTime(LocalDateTime.now());
        newsMapper.updateById(updateObj);
    }

    @Override
    public void unpublishNews(Long id) {
        // 校验存在
        validateNewsExists(id);
        // 更新状态为已下架
        NewsDO updateObj = new NewsDO();
        updateObj.setId(id);
        updateObj.setStatus(2);
        newsMapper.updateById(updateObj);
    }

} 