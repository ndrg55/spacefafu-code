package com.spacefafu.search.dao;

import com.spacefafu.search.pojo.Communion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface CommunionSearchDao  extends ElasticsearchRepository<Communion,String> {
    Page<Communion> findByTitleLikeOrContentLike(String title, String content, Pageable pageable);

    List<Communion> findByContentId(String contentid);
}
