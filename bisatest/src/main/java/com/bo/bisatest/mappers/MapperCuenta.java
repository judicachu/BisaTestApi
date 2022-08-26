package com.bo.bisatest.mappers;

import com.bo.bisatest.dto.CuentaDto;
import com.bo.bisatest.entities.Cuenta;

public class MapperCuenta {

	public static CuentaDto mapperCuentaToCuentaDto(Cuenta cuenta) {
		CuentaDto cuentaDto = new CuentaDto();
		cuentaDto.setCodigoMoneda(cuenta.getCodigoMoneda());
		cuentaDto.setIdPersona(cuenta.getId());
		cuentaDto.setEstado(cuenta.getEstado());
		cuentaDto.setFechaCreacion(cuenta.getFechaCreacion());
		cuentaDto.setNumeroCuenta(cuenta.getNumeroCuenta());
		cuentaDto.setSaldoDisponible(cuenta.getSaldoDisponible());
		return cuentaDto;
		
	}
}
