package com.bo.bisatest.mappers;

import com.bo.bisatest.dto.MovimientoDto;
import com.bo.bisatest.entities.Movimiento;

public class MovimientoMapper {

	
	public static Movimiento mapperMovimientoDtoToMovimiento(MovimientoDto movimientoDto,String tipoMovimiento){
		Movimiento movimiento = new Movimiento();
		movimiento.setEstado("ACT");
		movimiento.setIdCuenta(movimientoDto.getIdCuenta());
		movimiento.setMonto(movimientoDto.getMonto());
		movimiento.setTipoMovimiento(tipoMovimiento);
		return movimiento;
		
	}
	public static MovimientoDto mapperMovimientoToMovimientoDto(Movimiento movimiento) {
		MovimientoDto movimientoDto = new MovimientoDto();
		movimientoDto.setEstado(movimiento.getEstado());
		movimientoDto.setIdCuenta(movimiento.getIdCuenta());
		movimientoDto.setMonto(movimiento.getMonto());
		movimientoDto.setTipoMovimiento(movimiento.getTipoMovimiento());
		movimientoDto.setFecha(movimiento.getFecha());
		return movimientoDto;
	}
}
