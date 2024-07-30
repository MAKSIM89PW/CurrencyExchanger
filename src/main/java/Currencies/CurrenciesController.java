package Currencies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/valutes")
public class CurrenciesController {

    @Autowired
    private CurrencyRepository valuteRepository;

    @GetMapping
    public List<Currency> getAllValutes() {
        return valuteRepository.findAll();
    }

    @GetMapping("/{id}")
    public Currency getValuteById(@PathVariable Long id) {
        return valuteRepository.findById(id).orElseThrow(() -> new RuntimeException("Valute not found"));
    }

    @PostMapping
    public Currency createValute(@RequestBody Currency valute) {
        return valuteRepository.save(valute);
    }

    @PutMapping("/{id}")
    public Currency updateValute(@PathVariable Long id, @RequestBody Currency valute) {
        Currency existingValute = valuteRepository.findById(id).orElseThrow(() -> new RuntimeException("Valute not found"));
        existingValute.setCode(valute.getCode());
        existingValute.setFullName(valute.getFullName());
        existingValute.setSign(valute.getSign());
        return valuteRepository.save(existingValute);
    }

    @DeleteMapping("/{id}")
    public void deleteValute(@PathVariable Long id) {
        valuteRepository.deleteById(id);
    }
}
