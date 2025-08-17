package cn.iocoder.yudao.module.system.service.news;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.system.controller.admin.news.vo.NewsPageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.news.vo.NewsSaveReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.news.NewsDO;

import jakarta.validation.Valid;

/**
 * 新闻 Service 接口
 */
public interface NewsService {

    /**
     * 创建新闻
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createNews(@Valid NewsSaveReqVO createReqVO);

    /**
     * 更新新闻
     *
     * @param updateReqVO 更新信息
     */
    void updateNews(@Valid NewsSaveReqVO updateReqVO);

    /**
     * 删除新闻
     *
     * @param id 编号
     */
    void deleteNews(Long id);

    /**
     * 获得新闻
     *
     * @param id 编号
     * @return 新闻
     */
    NewsDO getNews(Long id);

    /**
     * 获得新闻分页
     *
     * @param pageReqVO 分页查询
     * @return 新闻分页
     */
    PageResult<NewsDO> getNewsPage(NewsPageReqVO pageReqVO);

    /**
     * 发布新闻
     *
     * @param id 编号
     */
    void publishNews(Long id);

    /**
     * 下架新闻
     *
     * @param id 编号
     */
    void unpublishNews(Long id);

} 