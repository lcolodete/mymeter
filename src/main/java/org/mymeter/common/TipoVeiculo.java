package org.mymeter.common;

public enum TipoVeiculo {

	CARRO(0),
	MOTO(1),
	CAMINHAO(3);
	
	TipoVeiculo(Integer codigo) {
		this.codigo = codigo;
	}
	
	private Integer codigo;

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
}
