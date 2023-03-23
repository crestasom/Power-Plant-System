package com.crestasom.pps.model;

import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
@Entity
@Table(name = "battery")
@Validated
public class Battery implements Comparable<Battery> {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	@NotBlank(message = "Name should not be empty")
	private String name;
	@Range(min = 1, message = "post code may not be empty or null")
	private Integer postCode;
	@Range(min = 1, message = "capacity may not be empty or null")
	private Integer capacity;

	@Override
	public int compareTo(Battery o) {
		return name.compareTo(o.getName());
	}

}
