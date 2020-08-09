package com.fh.shop.api.Area.biz;

import com.alibaba.fastjson.JSONObject;
import com.fh.shop.api.Area.mapper.AreaMapper;
import com.fh.shop.api.Area.po.Area;
import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.utils.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AreaServiceImpl implements AreaService {

    @Autowired
    private AreaMapper areaMapper;



    @Override
    public ServerResponse queryArea(Long id) {
        String list = RedisUtil.get("areaList");
        if (StringUtils.isNotEmpty(list)){
            List<Area> areaList = JSONObject.parseArray(list, Area.class);
            List<Area> queryChildList = queryChildList(areaList, id);
            return ServerResponse.success(queryChildList);
        }
        List<Area> areaList = areaMapper.selectList(null);
        if (areaList!=null){
            String jsonString = JSONObject.toJSONString(areaList);
            RedisUtil.set("areaList",jsonString);
        }
        List<Area> queryChildList = queryChildList(areaList, id);
        return ServerResponse.success(queryChildList);
    }

    public List<Area> queryChildList(List<Area> areaList,Long id ){
        List<Area> areaChild = new ArrayList<>();
        for (Area area : areaList) {
            if(area.getParentid().equals(id)){
                areaChild.add(area);
            }
        }
        return  areaChild;
    }
}
