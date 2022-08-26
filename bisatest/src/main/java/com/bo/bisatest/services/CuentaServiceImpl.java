package com.bo.bisatest.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bo.bisatest.dao.ICuentaDao;
import com.bo.bisatest.dao.IMovientoDao;
import com.bo.bisatest.dto.CuentaDto;
import com.bo.bisatest.dto.MovimientoDto;
import com.bo.bisatest.entities.Cuenta;
import com.bo.bisatest.entities.Movimiento;
import com.bo.bisatest.enums.EstadoEnum;
import com.bo.bisatest.mappers.MapperCuenta;
import com.bo.bisatest.mappers.MovimientoMapper;

@Service
public class CuentaServiceImpl implements ICuentaService {

	@Autowired
	private ICuentaDao cuentaDao;
	@Autowired
	private IMovientoDao movimientoDao;

	@Override
	public CuentaDto save(CuentaDto cuentaDto) {

		Cuenta cuenta = new Cuenta();
		cuenta.setCodigoMoneda(cuentaDto.getCodigoMoneda());
		cuenta.setIdPersona(cuentaDto.getIdPersona());
		cuenta.setNumeroCuenta(cuentaDto.getNumeroCuenta());
		cuenta.setEstado("ACTIVE");
		cuenta.setSaldoDisponible(0d);
		cuenta = cuentaDao.save(cuenta);
		return MapperCuenta.mapperCuentaToCuentaDto(cuenta);

	}

	@Override
	public CuentaDto findById(Long Id) {
		Cuenta cuenta = cuentaDao.findById(Id).orElse(null);
		if (cuenta != null) {
			return MapperCuenta.mapperCuentaToCuentaDto(cuenta);
		}
		return null;
	}

	@Override
	public CuentaDto depositarDinero(MovimientoDto movimientoDto) {

		Cuenta cuenta = cuentaDao.findById(movimientoDto.getIdCuenta()).orElse(null);
		Double montoFinal = 0d;
		if (cuenta != null) {
			if (cuenta.getCodigoMoneda().equals(movimientoDto.getMoneda())) {
				montoFinal = cuenta.getSaldoDisponible() + movimientoDto.getMonto();
				if (cuenta.getEstado().equals(EstadoEnum.HOLD.name())) {

					if (montoFinal >= 0) {
						cuenta.setEstado(EstadoEnum.ACTIVE.name());

					}

				}
				cuenta.setSaldoDisponible(montoFinal);
				cuentaDao.save(cuenta);
				Movimiento movimiento = MovimientoMapper.mapperMovimientoDtoToMovimiento(movimientoDto, "DEPOSITO");
				movimientoDao.save(movimiento);
			} else {

				return null;
			}

		} else {
			return null;
		}

		return MapperCuenta.mapperCuentaToCuentaDto(cuenta);
	}

	@Override
	public List<MovimientoDto> findByIdCuenta(Long Id) {

		List<Movimiento> list = movimientoDao.findByIdCuentaOrderByFechaDesc(Id);
		List<MovimientoDto> listDao = new ArrayList<>();

		for (Movimiento movimiento : list) {

			listDao.add(MovimientoMapper.mapperMovimientoToMovimientoDto(movimiento));
		}

		return listDao;
	}

	@Override
	public CuentaDto retirarDinero(MovimientoDto movimientoDto) {

		Cuenta cuenta = cuentaDao.findById(movimientoDto.getIdCuenta()).orElse(null);
		Double montoFinal = 0d;
		if (cuenta != null) {
			if (cuenta.getCodigoMoneda().equals(movimientoDto.getMoneda())) {
				montoFinal = cuenta.getSaldoDisponible() - movimientoDto.getMonto();
				if (cuenta.getEstado().equals(EstadoEnum.ACTIVE.name())) {
					if (montoFinal < 0) {
						cuenta.setEstado(EstadoEnum.HOLD.name());
					}
				} else {
					return null;
				}
				cuenta.setSaldoDisponible(montoFinal);
				cuentaDao.save(cuenta);
				Movimiento movimiento = MovimientoMapper.mapperMovimientoDtoToMovimiento(movimientoDto, "RETIRO");
				movimientoDao.save(movimiento);
			} else {

				return null;
			}

		} else {
			return null;
		}

		return MapperCuenta.mapperCuentaToCuentaDto(cuenta);
	}

}
