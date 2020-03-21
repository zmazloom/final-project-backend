package planning.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "teacher")
public class Teacher {

	@Id
    @Column(name = "id")
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;

	@Column
	private String firstName;

	@Column
	private String lastName;

	@Column
    private String prefix;

    @Column
    private String username;

    @Column
    private String password;

	@Column
    private String avatar;

	@Column
    private boolean removed;

    @Column(updatable = false)
    @CreationTimestamp
    private Date created;

}