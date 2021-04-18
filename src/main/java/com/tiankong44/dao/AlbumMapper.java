package com.tiankong44.dao;

import com.tiankong44.model.Album;
import com.tiankong44.model.vo.AlbumVo;
import net.sf.json.JSONObject;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface AlbumMapper {
    /**
     * 查询相册列表
     *
     * @param reqJson
     * @return
     * @author zhanghao_SMEICS
     * @Date 2021/4/8 16:41
     */

    List<Album> queryAlbumList(JSONObject reqJson);

    /**
     * 新增相册
     *
     * @param album
     * @return
     * @author zhanghao_SMEICS
     * @Date 2021/4/9 14:09
     */

    boolean addAlbum(Album album);

    /**
     * 记录日志
     *
     * @param reqJson
     * @return
     * @author zhanghao_SMEICS
     * @Date 14:59
     */

    void backupAlbum(JSONObject reqJson);

    /**
     * 删除相册
     *
     * @param reqJson
     * @return
     * @author zhanghao_SMEICS
     * @Date 2021/4/9 17:04
     */

    boolean deleteAlbum(JSONObject reqJson);
    /**
     * 相册详情  带图片
     *
     * @param reqJson
     * @return
     * @author zhanghao_SMEICS
     * @Date 2021/4/9 17:04
     */
    AlbumVo getAlbumDetail(JSONObject reqJson);
    /**
     *相册详情 仅相册   不带图片
     * @author zhanghao_SMEICS
     * @Date 2021/4/12 11:29
     * @param reqJson
     * @return
     */

    AlbumVo getJustAlbumDetail(JSONObject reqJson);
    /**
     * 搜索相册详情  带图片
     *
     * @param reqJson
     * @return
     * @author zhanghao_SMEICS
     * @Date 2021/4/9 17:04
     */
    AlbumVo queryAlbumDetail(JSONObject reqJson);
}
