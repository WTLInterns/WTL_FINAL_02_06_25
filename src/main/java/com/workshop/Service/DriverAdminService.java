package com.workshop.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.workshop.Entity.CabAdmin;
import com.workshop.Entity.DriveAdmin;
import com.workshop.Entity.OutSourceCarCab;
import com.workshop.Repo.DriverAdminRepo;
import com.workshop.Repo.VehicleRepository;

@Service
public class DriverAdminService {

	
	@Autowired
	private DriverAdminRepo driverAdminRepo;

    @Autowired
    private CloudinaryService cloudinaryService;
	
	// Directory where the images will be stored
    private static final String IMAGE_DIR = "src/main/resources/static/images/driverAdminImg/";

    

    // Create or Update vehicle with multiple images
    public DriveAdmin saveDriverAdmin(DriveAdmin driveAdmin,
                                       MultipartFile DriverImgSelfie,
                                       MultipartFile Aadhar,
                                       MultipartFile DrLicenceNum,
                                       MultipartFile PvcNo
                                      ) throws IOException {

                                        driveAdmin.setDriverImgSelfie(
                                            DriverImgSelfie != null && !DriverImgSelfie.isEmpty() 
                                                ? cloudinaryService.upload(DriverImgSelfie) 
                                                : null
                                        );
                                        
                                        driveAdmin.setAadhar(
                                            Aadhar != null && !Aadhar.isEmpty() 
                                                ? cloudinaryService.upload(Aadhar) 
                                                : null
                                        );
                                        
                                        driveAdmin.setDrLicenceNum(
                                            DrLicenceNum != null && !DrLicenceNum.isEmpty() 
                                                ? cloudinaryService.upload(DrLicenceNum) 
                                                : null
                                        );
                                        
                                        driveAdmin.setPvcNo(
                                            PvcNo != null && !PvcNo.isEmpty() 
                                                ? cloudinaryService.upload(PvcNo) 
                                                : null
                                        );
                                        
    	
    	

        // Debugging: print the vehicle object to verify its state before saving
        System.out.println("Vehicle object to save: " + driveAdmin);

        return driverAdminRepo.save(driveAdmin); // Save the vehicle details with the image paths
    }

    // Get all vehicles
    public List<DriveAdmin> getDriverAdmin() {
        return driverAdminRepo.findAll();
    }

    // Get vehicle by ID
    public Optional<DriveAdmin> getDriverAdminById(int id) {
        return driverAdminRepo.findById(id);
    }

//    // Delete vehicle by ID
//    public void deleteVehicle( id) {
//    	driverAdminRepo.deleteById(id);
//    }

    // Helper method to save the image and return the file name
    private String saveImage(MultipartFile image, String imageType) throws IOException {
        if (image != null && !image.isEmpty()) {
            // Create directory if not exists
            File directory = new File(IMAGE_DIR);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Get the file name, adding a timestamp to avoid collisions
            String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            Path destinationPath = Path.of(directory.getAbsolutePath(), fileName);

            // Save the image to the file system
            Files.copy(image.getInputStream(), destinationPath, StandardCopyOption.REPLACE_EXISTING);

            // Log file name
            System.out.println(imageType + " saved with file name: " + fileName);

            return fileName;  // Return the saved file name
        }

        // Return a default image if no file is provided
        return "default.jpg";
    }

    
	 public DriveAdmin udpateStatus(int id, String status) {
    	DriveAdmin cab = this.driverAdminRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Cart not found"));
    	cab.setStatus(status);
		    return driverAdminRepo.save(cab);
    }

    public List<DriveAdmin> getCabByStatus(String status) {
    return driverAdminRepo.findAll().stream() // Use stream to filter
            .filter(c -> c.getStatus().equals(status)) // Filter by status
            .collect(Collectors.toList()); // Collect the filtered results into a list
}
	
}
