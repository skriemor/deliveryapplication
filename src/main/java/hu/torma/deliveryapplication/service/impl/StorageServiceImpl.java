package hu.torma.deliveryapplication.service.impl;

import hu.torma.deliveryapplication.DTO.DisplayUnit;
import hu.torma.deliveryapplication.DTO.ProductDTO;
import hu.torma.deliveryapplication.DTO.SaleDTO;
import hu.torma.deliveryapplication.repository.PurchaseRepository;
import hu.torma.deliveryapplication.repository.SaleRepository;
import hu.torma.deliveryapplication.service.StorageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class StorageServiceImpl implements StorageService {
    Logger log = Logger.getLogger("Fos");

    ModelMapper mapper = new ModelMapper();

    @Autowired
    PurchaseRepository pRepo;

    @Autowired
    SaleRepository sRepo;

    @Override
    public Integer getSupplyOf(ProductDTO dto, SaleDTO saleToIgnore) {
        int amount = 0;
        amount = pRepo.findAll().stream().flatMap(c->c.getProductList().stream())
                .filter(f->f.getProduct().getId().equals(dto.getId()))
                .map(m->m.getQuantity2())
                .collect(Collectors.summingInt(Integer::intValue));

        amount -= sRepo.findAll().stream()
                .filter(g->saleToIgnore.getId() != g.getId())
                .flatMap(c->c.getProductList().stream())
                .filter(f->f.getProduct().getId().equals(dto.getId()))
                .map(m->m.getQuantity2())
                .collect(Collectors.summingInt(Integer::intValue));
        return amount;
    }

    private List<String> prodList = Arrays.asList("I.OSZTÁLYÚ","II.OSZTÁLYÚ","III.OSZTÁLYÚ","IV.OSZTÁLYÚ","IPARI", "GYÖKÉR");

    @Override
    public ArrayList<DisplayUnit> getDisplayUnits() {
        int amount = 0, verifiedAmount = 0;
        ArrayList<DisplayUnit> list = new ArrayList<>();
        var tempPList = pRepo.findAll().stream().flatMap(c->c.getProductList().stream()).toList();
        var tempSList = sRepo.findAll().stream().flatMap(c->c.getProductList().stream()).toList();
        var tempSListVerified = sRepo.findAll().stream().filter(a->a.getBuyer().getPaper().equals("Igen")).flatMap(c->c.getProductList().stream()).toList();
        for (var p: prodList) {
            amount = 0; verifiedAmount=0;
            for (var a: tempPList) {
                if (p.equals(a.getProduct().getId())) {
                    amount += a.getQuantity2();
                    verifiedAmount += a.getQuantity2() - a.getActual();
                }
            }
            for(var a: tempSList) {
                if (p.equals(a.getProduct().getId())) {
                    amount -= a.getQuantity();
                    //verifiedAmount -= a.getActual();
                }
            }
            for(var a: tempSListVerified) {
                if (p.equals(a.getProduct().getId())) {
                    //amount -= a.getQuantity();
                    verifiedAmount -= a.getQuantity();
                }
            }
/*
            amount = pRepo.findAll().stream().flatMap(c->c.getProductList().stream())
                    .filter(f->f.getProduct().getId().equals(p))
                    .map(m->m.getQuantity2())
                    .collect(Collectors.summingInt(Integer::intValue));

            amount -= sRepo.findAll().stream().flatMap(c->c.getProductList().stream())
                    .filter(f->f.getProduct().getId().equals(p))
                    .map(m->m.getQuantity())
                    .collect(Collectors.summingInt(Integer::intValue));
*/
            list.add(new DisplayUnit(amount,verifiedAmount,p));
        }

        return list;
    }


}
