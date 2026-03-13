package org.fizzy.moviedate.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.fizzy.moviedate.entity.MovieDiary;

/**
 * 电影日记 Mapper
 */
@Mapper
public interface MovieDiaryMapper extends BaseMapper<MovieDiary> {
    
}
