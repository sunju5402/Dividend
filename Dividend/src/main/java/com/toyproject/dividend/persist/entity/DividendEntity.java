package com.toyproject.dividend.persist.entity;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity(name = "DIVIDEND")
@Getter
@ToString
@NoArgsConstructor
public class DividendEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long companyId;

	private LocalDateTime date;

	private String dividend;
}
