package com.mossle.domap.manager;

import org.springframework.stereotype.Service;

import com.mossle.core.hibernate.HibernateEntityDao;
import com.mossle.domap.domain.MapInfo;
@Service
public class MapInfoManager extends HibernateEntityDao<MapInfo> {

}
