package com.example.application.imageupload.storage;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;
import java.io.File;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.application.constructionsite.ConstructionSiteRepository;
import com.example.application.constructionsitedetails.ConstructionSiteDetailsRepository;
import com.example.application.restriction.Restriction;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileSystemStorageService implements StorageService {

	public enum Elements {
		TRANSFORMERS(0),
		EXPANSION_TANK(1), 
		RADIATOR(2),
		CONNECTION_POINT(3),
		FIREWALL(4);
		private final int index;
	
		Elements(int index) {
			this.index = index;
		}
	
		public int getIndex() {
			return this.index;
		}
	}

	private final Path rootLocation;
	private final Path resultsLocation;
	private final static String FILE_RESPONSE_LOCATION = "http://localhost:8080/api/";
	private final static Path AI_PREDICTING_FILE_LOCATION = Path.of("src", "main", "yolov8", "predictimg.py");
	private final ConstructionSiteRepository constructionSiteRepository;
	private final ConstructionSiteDetailsRepository constructionSiteDetailsRepository;

	@Autowired
	public FileSystemStorageService(StorageProperties properties, ConstructionSiteRepository constructionSiteRepository, 
	ConstructionSiteDetailsRepository constructionSiteDetailsRepository) {
        
        if(properties.getLocation().trim().length() == 0){
            throw new StorageException("File upload location can not be Empty."); 
        }

		this.rootLocation = Paths.get(properties.getLocation());
		this.resultsLocation = Paths.get(properties.getResultsLocation());
		this.constructionSiteRepository = constructionSiteRepository;
		this.constructionSiteDetailsRepository = constructionSiteDetailsRepository;
	}

	@Override
	public void store(MultipartFile file) {
		try {
			if (file.isEmpty()) {
				throw new StorageException("Fallo al almacenar archivo vacío.");
			}

			Path destinationFile = this.rootLocation.resolve(
					Paths.get(file.getOriginalFilename()))
					.normalize().toAbsolutePath();
			if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
				throw new StorageException(
						"No se puede almacenar el archivo fuera del directorio.");
			}
			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, destinationFile,
					StandardCopyOption.REPLACE_EXISTING);
			}
		}
		catch (IOException e) {
			throw new StorageException("Fallo al almacenar el archivo.", e);
		}
	}

	@Override
	public Stream<Path> loadAll() {
		try {
			return Files.walk(this.resultsLocation, 1)
				.filter(path -> !path.equals(this.resultsLocation))
				.map(this.resultsLocation::relativize);
		}
		catch (IOException e) {
			throw new StorageException("Fallo al leer archivos almacenados", e);
		}

	}

	@Override
	public Path load(String filename) {
		return resultsLocation.resolve(filename);
	}

	@Override
	public Resource loadAsResource(String filename) {
		try {
			Path file = load(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			}
			else {
				throw new StorageFileNotFoundException(
						"No se pudo leer el archivo: " + filename);

			}
		}
		catch (MalformedURLException e) {
			throw new StorageFileNotFoundException("No se pudo leer el archivo: " + filename, e);
		}
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(rootLocation.toFile());
	}

	@Override
	public void init() {
		try {
			Files.createDirectories(rootLocation);
			Files.createDirectories(resultsLocation);
		}
		catch (IOException e) {
			throw new StorageException("No se pudo inicializar el almacenamiento", e);
		}
	}

	@Override
	public List<String> trim(List<String> strings) {
		List<String> trimmedStrings = new ArrayList<>(strings.size());
    
		for (String string : strings) {
			trimmedStrings.add(string.substring(FILE_RESPONSE_LOCATION.length()));
		}
		return trimmedStrings;
	}

	@Override
	public void processFile(String filename, String csId) {
		var details = constructionSiteRepository.getReferenceById(Long.parseLong(csId)).getDetails();
		ProcessBuilder processBuilder = new ProcessBuilder("python", AI_PREDICTING_FILE_LOCATION.toAbsolutePath().toString(), filename);
		processBuilder.redirectErrorStream(true);

		Process process;
		try {
			process = processBuilder.start();		
			process.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}

		details.setLastDayUploaded(LocalDateTime.now());

		int[] objectCounts = readObjectCounts(filename);
		List<String> collectedErrors = new ArrayList<>();

		for (Restriction r : details.getAllRestrictions()) {
			if (new Date().before(Date.from(r.getEndDate().atZone(ZoneId.systemDefault()).toInstant()))){
				if (new Date().after(Date.from(r.getStartDate().atZone(ZoneId.systemDefault()).toInstant()))) {
					if (r.isShouldAppear())
					{
						if (objectCounts[Elements.TRANSFORMERS.index] < r.getTransformers())
						{
							collectedErrors.add("Los transformadores no pueden ser menos de los requeridos (" + r.getTransformers() + ").");
						}
						if (objectCounts[Elements.EXPANSION_TANK.index] < r.getExpansionTanks()) 
						{
							collectedErrors.add("Los tanques de expansión no pueden ser menos de los requeridos (" + r.getExpansionTanks() + ").");
						}
						if (objectCounts[Elements.RADIATOR.index] < r.getRadiators()) 
						{
							collectedErrors.add("Los radiadores no pueden ser menos de los requeridos (" + r.getRadiators() + ").");
						}
						if (objectCounts[Elements.CONNECTION_POINT.index] < r.getConnectionPoints()) 
						{
							collectedErrors.add("Los puntos de conexión no pueden ser menos de los requeridos (" + r.getConnectionPoints() + ").");
						}
						if (objectCounts[Elements.FIREWALL.index] < r.getFirewalls()) 
						{
							collectedErrors.add("Los muros cortafuegos no pueden ser menos de los requeridos (" + r.getFirewalls() + ").");
						}
					}
					else{
						if (objectCounts[Elements.TRANSFORMERS.index] > r.getTransformers())
						{
							collectedErrors.add("Los transformadores no pueden ser más de los requeridos (" + r.getTransformers() + ").");
						}
						if (objectCounts[Elements.EXPANSION_TANK.index] > r.getExpansionTanks()) 
						{
							collectedErrors.add("Los tanques de expansión no pueden ser más de los requeridos (" + r.getExpansionTanks() + ").");
						}
						if (objectCounts[Elements.RADIATOR.index] > r.getRadiators()) 
						{
							collectedErrors.add("Los radiadores no pueden ser más de los requeridos (" + r.getRadiators() + ").");
						}
						if (objectCounts[Elements.CONNECTION_POINT.index] > r.getConnectionPoints()) 
						{
							collectedErrors.add("Los puntos de conexión no pueden ser más de los requeridos (" + r.getConnectionPoints() + ").");
						}
						if (objectCounts[Elements.FIREWALL.index] > r.getFirewalls()) 
						{
							collectedErrors.add("Los muros cortafuegos no pueden ser más de los requeridos (" + r.getFirewalls() + ").");
						}
					}
				}
			}
        }

		details.setRestrictionsViolated(collectedErrors);

		details.setNumberOfTransformers(details.getNumberOfTransformers() + objectCounts[Elements.TRANSFORMERS.index]);
		details.setNumberOfExpansionTanks(details.getNumberOfExpansionTanks() + objectCounts[Elements.EXPANSION_TANK.index]);
		details.setNumberOfRadiators(details.getNumberOfRadiators() + objectCounts[Elements.RADIATOR.index]);
		details.setNumberOfConnectionPoints(details.getNumberOfConnectionPoints() + objectCounts[Elements.CONNECTION_POINT.index]);
		details.setNumberOfFirewalls(details.getNumberOfFirewalls() + objectCounts[Elements.FIREWALL.index]);

		this.constructionSiteDetailsRepository.save(details);
	}

	private int[] readObjectCounts(String filename){
		String jsonFilePath = "storefiles/results/ObjectCounts_" + filename + ".json";
		ObjectMapper objectMapper = new ObjectMapper();
		int[] objectCounts = new int[Elements.values().length];

		try {	
			objectCounts = objectMapper.readValue(new File(jsonFilePath), int[].class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return objectCounts;
	}
}