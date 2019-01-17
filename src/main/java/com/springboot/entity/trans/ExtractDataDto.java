package com.springboot.entity.trans;

import com.springboot.entity.database.ElasticsearchDto;

public class ExtractDataDto {
	private TableInputDto tiDto;
	private ElasticsearchDto esDto;
	public TableInputDto getTiDto() {
		return tiDto;
	}
	public void setTiDto(TableInputDto tiDto) {
		this.tiDto = tiDto;
	}
	public ElasticsearchDto getEsDto() {
		return esDto;
	}
	public void setEsDto(ElasticsearchDto esDto) {
		this.esDto = esDto;
	}
}
