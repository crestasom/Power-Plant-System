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
@NoArgsConstructor
@Builder
@Entity
@Data
@Table(name = "battery")
@Validated
public class Battery implements Comparable<Battery> {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	@NotBlank(message = "Name should not be empty")
	private String name;
	@Range(min = 1, message = "post code may not be empty or null")
	private Integer postcode;
	@Range(min = 1, message = "capacity may not be empty or null")
	private Integer capacity;

	public int compareTo(Battery o) {
		return name.compareTo(o.getName());
	}

	@Override
	public String toString() {
		return "Battery [id=" + id + ", name=" + getName() + ", postCode=" + getPostcode() + ", capacity="
				+ getCapacity() + "]";
	}

}
