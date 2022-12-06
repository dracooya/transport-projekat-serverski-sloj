package rs.ac.uns.ftn.transport.mapper.panic;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import rs.ac.uns.ftn.transport.dto.PanicDTO;
import rs.ac.uns.ftn.transport.dto.panic.PanicReasonDTO;
import rs.ac.uns.ftn.transport.model.Panic;

public class PanicReasonDTOMapper {

    private static ModelMapper modelMapper;

    @Autowired
    public PanicReasonDTOMapper(ModelMapper modelMapper){this.modelMapper = modelMapper;}

    public static Panic fromDTOtoPanic(PanicDTO dto) {return modelMapper.map(dto,  Panic.class);}

    public static PanicReasonDTO fromPanicToDTO(Panic dto) {return modelMapper.map(dto, PanicReasonDTO.class);}
}
