package tech.yobbo.video.dao;

import java.util.List;
import java.util.Map;

/**
 * Created by xiaoJ on 5/16/2017.
 */
public interface VideoDao {

    List<Map<String, String>> queryVideoAll();
}
