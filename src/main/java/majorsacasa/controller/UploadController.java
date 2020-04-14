package majorsacasa.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class UploadController {

    // Inyectamos el valor configurado en application.properties a la variable
    @Value("${upload.file.directory}")
    private String uploadDirectory;

    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public String prepareUpload(Model model) {
        // Si hay que hacer algún chequeo o si la vista upload.html
        // necesita algún tipo de información se añade aquí
        return "upload";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String singleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        if (file.isEmpty()) {
            // Enviar mensaje de error porque no hay fichero seleccionado
            redirectAttributes.addFlashAttribute("message",
                    "Please select a file to upload");
            return "redirect:/uploadStatus";
        }

        try {
            // Obtener el fichero y guardarlo
            byte[] bytes = file.getBytes();
            Path path = Paths.get(uploadDirectory + "pdfs/"
                    + file.getOriginalFilename());
            Files.write(path, bytes);

            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded '" + path + "'");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/uploadStatus";
    }

}
