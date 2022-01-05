package com.kerellpnz.tnnwebdatabase.dao;

import com.kerellpnz.tnnwebdatabase.entity.BaseEntity;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@Repository
public class BaseEntityDAO<T extends BaseEntity> extends BaseDAO<T>{

}
