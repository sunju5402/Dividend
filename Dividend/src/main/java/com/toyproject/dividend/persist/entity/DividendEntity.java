package com.toyproject.dividend.persist.entity;

import com.toyproject.dividend.model.Dividend;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity(name = "DIVIDEND")
@Getter
@ToString
@NoArgsConstructor
@Table(
	uniqueConstraints = {
		@UniqueConstraint(
			columnNames = {"companyId", "date"}
		)
	}
)
public class DividendEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long companyId;

	private LocalDateTime date;

	private String dividend;

	public DividendEntity(Long companyId, Dividend dividend) {
		this.companyId = companyId;
		this.date = dividend.getDate();
		this.dividend = dividend.getDividend();
	}
}
