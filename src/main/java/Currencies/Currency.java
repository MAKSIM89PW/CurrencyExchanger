package Currencies;
import lombok.Data;


import javax.persistence.*;

@Entity
@Data
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String fullName;
    private String sign;
}

