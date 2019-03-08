package com.enigma.task.bootcamp;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enigma.task.bootcamp.dao.BootcampPeriodDao;
import com.enigma.task.bootcamp.dto.BootcampPeriodDto;
import com.enigma.task.bootcamp.dto.CommonResponse;
import com.enigma.task.bootcamp.exception.CustomExecption;
import com.enigma.task.bootcamp.model.BootcampPeriod;

@RestController
@RequestMapping("/bootcampperiod")
@SuppressWarnings("rawtypes")
public class BootcampPeriodController {
	
	@Autowired
	public ModelMapper modelMapper;
	
	@Autowired
	public BootcampPeriodDao bootcampPeriodDao;
	
	@GetMapping(value="/{bootcampperiodid}")
	public CommonResponse<BootcampPeriodDto> getById(@PathVariable("bootcampperiodid") String bcPeriodId) throws CustomExecption {
		try {
			BootcampPeriod bootcampPeriod = bootcampPeriodDao.getById(Integer.parseInt(bcPeriodId));
			
			return new CommonResponse<BootcampPeriodDto>(modelMapper.map(bootcampPeriod, BootcampPeriodDto.class));
		} catch (CustomExecption e) {
			return new CommonResponse<BootcampPeriodDto>("06", e.getMessage());
		} catch (Exception e) {
			return new CommonResponse<BootcampPeriodDto>("14", e.getMessage());
		}
	}
	
	@PostMapping(value="")
	public CommonResponse<BootcampPeriodDto> input(@RequestBody BootcampPeriodDto bootcampPeriodDto) throws CustomExecption {
		try {
			BootcampPeriod bootcampPeriod = modelMapper.map(bootcampPeriodDto, BootcampPeriod.class);
			bootcampPeriod.setId(0);
			bootcampPeriod = bootcampPeriodDao.save(bootcampPeriod);
			
			return new CommonResponse<BootcampPeriodDto>(modelMapper.map(bootcampPeriod, BootcampPeriodDto.class));
		} catch (CustomExecption e) {
			return new CommonResponse<BootcampPeriodDto>("14", "study period not found");
		} catch (NumberFormatException e) {
			return new CommonResponse<BootcampPeriodDto>();
		} catch (Exception e) {
			return new CommonResponse<BootcampPeriodDto>();
		}
	}
	
	@PutMapping(value="")
	public CommonResponse<BootcampPeriodDto> update(@RequestBody BootcampPeriodDto bootcampPeriodDto) throws CustomExecption {
		try {
			BootcampPeriod checkBootcampPeriod = bootcampPeriodDao.getById(bootcampPeriodDto.getId());
			if (checkBootcampPeriod == null) {
				return new CommonResponse<BootcampPeriodDto>("14", "bootcamp id not found");
			}
			if (bootcampPeriodDto.getBatchId() != null) {
				checkBootcampPeriod.setBatch_id(bootcampPeriodDto.getBatchId());
			}
			if (bootcampPeriodDto.getDescription() != null) {
				checkBootcampPeriod.setDescription(bootcampPeriodDto.getDescription());
			}
			if (bootcampPeriodDto.getWeekId() != null) {
				checkBootcampPeriod.setWeek_id(bootcampPeriodDto.getWeekId());
			}
			checkBootcampPeriod.setActive_flag(bootcampPeriodDto.isActiveFlag());
			checkBootcampPeriod = bootcampPeriodDao.save(checkBootcampPeriod);
			
			return new CommonResponse<BootcampPeriodDto>(modelMapper.map(checkBootcampPeriod, BootcampPeriodDto.class));
		} catch (CustomExecption e) {
			return new CommonResponse("01", e.getMessage());
		} catch (Exception e) {
			return new CommonResponse("06", e.getMessage());
		}
	}
	
	@DeleteMapping(value="/{bootcampperiodid}")
	public CommonResponse<BootcampPeriodDto> delete(@PathVariable("bootcampperiodid") String id) throws CustomExecption {
		try {

			BootcampPeriod bootcampPeriod = bootcampPeriodDao.getById(Integer.parseInt(id));

			if (bootcampPeriod == null) {
				return new CommonResponse("06", "trainee not found");
			}
			
			bootcampPeriodDao.delete(bootcampPeriod);
			return new CommonResponse<BootcampPeriodDto>();
			
		} catch (CustomExecption e) {
			return new CommonResponse("01", e.getMessage());
		} catch (Exception e) {
			return new CommonResponse("06", e.getMessage());
		}
	}
	
	@GetMapping("")
	public CommonResponse<List<BootcampPeriodDto>> getList(@RequestParam(name="list", defaultValue="") String id) throws CustomExecption {
		try {
			List<BootcampPeriod> bootcampPeriods = bootcampPeriodDao.getList();
			
			return new CommonResponse<List<BootcampPeriodDto>>(bootcampPeriods.stream()
					.map(bcPeriod -> modelMapper.map(bcPeriod, BootcampPeriodDto.class))
					.collect(Collectors.toList()));
		} catch (CustomExecption e) {
			throw e;
		} catch(NumberFormatException e) {
			return new CommonResponse("01", e.getMessage());
		} catch (Exception e) {
			return new CommonResponse("06", e.getMessage());
		}
	}
	
	@GetMapping("/active")
	public CommonResponse<List<BootcampPeriodDto>> getListByActiveFlag(@RequestParam(name="list", defaultValue="") String id) throws CustomExecption {
		try {
			List<BootcampPeriod> bootcampPeriods = bootcampPeriodDao.getList();
			
			return new CommonResponse<List<BootcampPeriodDto>>(bootcampPeriods.stream()
					.map(bcPeriod -> modelMapper.map(bcPeriod, BootcampPeriodDto.class))
					.collect(Collectors.toList()));
		} catch (CustomExecption e) {
			throw e;
		} catch(NumberFormatException e) {
			return new CommonResponse("01", e.getMessage());
		} catch (Exception e) {
			return new CommonResponse("06", e.getMessage());
		}
	}
	
}
