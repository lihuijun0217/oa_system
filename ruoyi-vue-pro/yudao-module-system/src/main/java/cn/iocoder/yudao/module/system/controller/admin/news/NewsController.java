package cn.iocoder.yudao.module.system.controller.admin.news;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.system.controller.admin.news.vo.NewsPageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.news.vo.NewsRespVO;
import cn.iocoder.yudao.module.system.controller.admin.news.vo.NewsSaveReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.news.NewsDO;
import cn.iocoder.yudao.module.system.service.news.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 新闻")
@RestController
@RequestMapping("/system/news")
@Validated
public class NewsController {

    @Resource
    private NewsService newsService;

    @PostMapping("/create")
    @Operation(summary = "创建新闻")
    @PreAuthorize("@ss.hasPermission('system:news:create')")
    public CommonResult<Long> createNews(@Valid @RequestBody NewsSaveReqVO createReqVO) {
        return success(newsService.createNews(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "修改新闻")
    @PreAuthorize("@ss.hasPermission('system:news:update')")
    public CommonResult<Boolean> updateNews(@Valid @RequestBody NewsSaveReqVO updateReqVO) {
        newsService.updateNews(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除新闻")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:news:delete')")
    public CommonResult<Boolean> deleteNews(@RequestParam("id") Long id) {
        newsService.deleteNews(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得新闻")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<NewsRespVO> getNews(@RequestParam("id") Long id) {
        NewsDO news = newsService.getNews(id);
        return success(BeanUtils.toBean(news, NewsRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得新闻分页")
    public CommonResult<PageResult<NewsRespVO>> getNewsPage(@Valid NewsPageReqVO pageReqVO) {
        PageResult<NewsDO> pageResult = newsService.getNewsPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, NewsRespVO.class));
    }

    @PostMapping("/publish")
    @Operation(summary = "发布新闻")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:news:update')")
    public CommonResult<Boolean> publishNews(@RequestParam("id") Long id) {
        newsService.publishNews(id);
        return success(true);
    }

    @PostMapping("/unpublish")
    @Operation(summary = "下架新闻")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:news:update')")
    public CommonResult<Boolean> unpublishNews(@RequestParam("id") Long id) {
        newsService.unpublishNews(id);
        return success(true);
    }

} 