package tech.yobbo.video.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by xiaoJ on 2017/5/11.
 * 视频播放控制器
 */
@Controller
@RequestMapping(value = "/mv")
public class VideoController {

    /**
     * 路径跳转到视频播放首页
     * @return
     */
    @RequestMapping
    public ModelAndView toVideoIndex(){
        ModelAndView videoIndexView = new ModelAndView("video/index");
        return videoIndexView;
    }

    @RequestMapping(value = "/detail")
    public ModelAndView toVideoDetail(){
        ModelAndView videoDetail = new ModelAndView("video/detail");
        return videoDetail;
    }
}
