package intern.sapo.be.dto.request;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Getter
@Setter
public class AccountDTO {
	private Integer id;
	private String username;
	private String password;
	private java.sql.Timestamp createAt;
	private java.sql.Timestamp updateAt;
	private Boolean isDelete;
	private List<String> roleIds;
	private String fullName;
	private String image;
	private String email;
	private String phone;
	private String address;
	private Long accountId;

}
