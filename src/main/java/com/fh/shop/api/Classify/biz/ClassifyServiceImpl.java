package com.fh.shop.api.Classify.biz;

import com.fh.shop.api.Classify.mapper.ClassifyMapper;
import com.fh.shop.api.Classify.po.Classify;
import com.fh.shop.api.common.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassifyServiceImpl implements ClassifyService {

    @Autowired
    private ClassifyMapper classifyMapper;
    @Override
    public ServerResponse queryClassIfylist() {
        List<Classify> classifyList = classifyMapper.selectList(null);
        return ServerResponse.success(classifyList);
    }
}
