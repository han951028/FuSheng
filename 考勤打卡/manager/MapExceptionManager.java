package com.mossle.domap.manager;

import org.springframework.stereotype.Service;

import com.mossle.core.hibernate.HibernateEntityDao;
import com.mossle.domap.domain.MapException;
import com.mossle.domap.domain.MapGroup;
@Service
public class MapExceptionManager extends HibernateEntityDao<MapException> {

}
