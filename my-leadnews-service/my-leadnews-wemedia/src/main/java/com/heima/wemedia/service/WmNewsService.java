package com.heima.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmNewsDto;
import com.heima.model.wemedia.dtos.WmNewsPageReqDto;
import com.heima.model.wemedia.pojos.WmNews;

import java.lang.reflect.InvocationTargetException;

public interface WmNewsService extends IService<WmNews> {

    /**
     * 条件查询文章列表
     */
    public ResponseResult findList(WmNewsPageReqDto dto);

    /**
     * 保存发布文章
     * @param dto
     * @return
     */
    public ResponseResult submitNews(WmNewsDto dto);

    /**
     * 文章的上下架
     * @param dto
     * @return
     */
    public ResponseResult downOrUp(WmNewsDto dto);
}
