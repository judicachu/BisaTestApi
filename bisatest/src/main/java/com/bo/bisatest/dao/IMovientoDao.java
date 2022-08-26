package com.bo.bisatest.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bo.bisatest.entities.Movimiento;

public interface IMovientoDao extends CrudRepository<Movimiento, Long>{

	public  List<Movimiento> findByIdCuentaOrderByFechaDesc(Long idCuenta);
}
