package com.bo.bisatest.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bo.bisatest.dto.CuentaDto;
import com.bo.bisatest.dto.MovimientoDto;
import com.bo.bisatest.services.ICuentaService;

@RestController
@RequestMapping("/api/cuenta")
public class CuentaRestController {

	@Autowired
	private ICuentaService cuentaService;


	@PostMapping("/crear-cuenta")
	public ResponseEntity<?> create(@Valid @RequestBody CuentaDto cuentaDto, BindingResult result) {

		CuentaDto cuentaNueva = null;
		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream()
					.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());

			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);

		}

		try {
			cuentaNueva = cuentaService.save(cuentaDto);

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al crear la cuenta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);

		}

		response.put("mensaje", "La cuenta ha sido creada con exito");
		response.put("cuenta", cuentaNueva);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PostMapping("/depositar-dinero")
	public ResponseEntity<?> depositarDinero(@Valid @RequestBody MovimientoDto movimientoDto, BindingResult result) {
		CuentaDto cuentaDto = null;
		Map<String, Object> response = new HashMap<>();
		try {

			cuentaDto = cuentaService.depositarDinero(movimientoDto);
			if (cuentaDto == null) {

				response.put("mensaje", "Error en los datos de la cuenta");
				// response.put("cuenta", cuenta);

				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.ACCEPTED);
			}

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar el depósito");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);

		}

		response.put("mensaje", "EL doposito ha sido realizado con exito con exito");
		response.put("cuenta", cuentaDto);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@PostMapping("/retirar-dinero")
	public ResponseEntity<?> retirarDinero(@Valid @RequestBody MovimientoDto movimientoDto, BindingResult result) {
		CuentaDto cuentaDto = null;
		Map<String, Object> response = new HashMap<>();
		try {

			cuentaDto = cuentaService.retirarDinero(movimientoDto);
			if (cuentaDto == null) {

				response.put("mensaje", "Error en los datos de la cuenta");

				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.ACCEPTED);
			}

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar el retiro");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);

		}

		response.put("mensaje", "El retiro ha sido realizado con exito con exito");
		response.put("cuenta", cuentaDto);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/consultar-saldo/{id}")
	public ResponseEntity<?> consultarSaldo(@PathVariable Long id) {

		CuentaDto cuentaDto = null;
		Map<String, Object> response = new HashMap<>();
		try {
			cuentaDto = cuentaService.findById(id);

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al encontrar la cuenta!");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (cuentaDto == null) {
			response.put("mensaje", "La cuenta ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		response.put("mensaje", "Consulta de saldo exitosa!");
		response.put("cuenta", cuentaDto);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@GetMapping("/historico-transacciones/{id}")
	public ResponseEntity<?> consultarHistorico(@PathVariable Long id) {

		CuentaDto cuentaDto = null;
		List<MovimientoDto> listDto =null;
		Map<String, Object> response = new HashMap<>();
		try {
			cuentaDto = cuentaService.findById(id);

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al encontrar la cuenta!");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (cuentaDto == null) {
			response.put("mensaje", "La cuenta ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		else {
			try {
				listDto = cuentaService.findByIdCuenta(id);

			} catch (DataAccessException e) {
				response.put("mensaje", "Error al encontrar movimiento de la cuenta!");
				response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
		}
		response.put("mensaje", "Consulta de historicos exitosa!");
		response.put("cuenta", cuentaDto);
		response.put("historico", listDto);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	
}
