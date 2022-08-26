package com.bo.bisatest.services;

import java.util.List;

import com.bo.bisatest.dto.CuentaDto;
import com.bo.bisatest.dto.MovimientoDto;


public interface ICuentaService {


	public CuentaDto save(CuentaDto cuentaDto);

	public CuentaDto findById(Long Id);
	
	public List<MovimientoDto>findByIdCuenta(Long Id);
	
	public CuentaDto depositarDinero(MovimientoDto movimientoDto);
	
	public CuentaDto retirarDinero(MovimientoDto movimientoDto);
	
}
