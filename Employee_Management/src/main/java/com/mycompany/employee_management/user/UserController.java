package com.mycompany.employee_management.user;

import com.cloudinary.utils.ObjectUtils;
import com.mycompany.employee_management.MiddleWare.AuthMiddleWare;
import com.mycompany.employee_management.PermissionConstant.PermissionConstant;
import com.mycompany.employee_management.config.CloudinaryConfig;
import com.mycompany.employee_management.exception.UnauthorizedException;

import com.mycompany.employee_management.security.JwtUtil;
import com.mycompany.employee_management.user.forgetPassword.ForgetPasswordRequest;
import com.mycompany.employee_management.user.forgetPassword.SendForgetPasswordEmail;

import com.mycompany.employee_management.user.loginPojo.LoginRequest;
import com.mycompany.employee_management.user.passwordChange.ChangePasswordRequest;

import io.javalin.Javalin;
import io.javalin.http.UploadedFile;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import java.util.List;

import java.util.Map;
import java.util.UUID;

/**
 *
 * @author sachi
 */
public class UserController {

    private final UserService userService = (UserService) new UserServiceImplementation();

    public UserController(Javalin app) {

        app.before("/user", AuthMiddleWare.requireLogin);

        app.post("/login", ctx -> {

            LoginRequest loginRequest = ctx.bodyAsClass(LoginRequest.class);

            ctx.status(200).json(userService.login(loginRequest));
        });

        app.get("/logout", ctx -> {

            ctx.json(userService.logout()).status(200);

        });

        app.get("/user", ctx -> {
            AuthMiddleWare.requirePermission(PermissionConstant.READ_USER).handle(ctx);
            ctx.status(200).json(userService.getAllUser());

        });

        app.post("/user", ctx -> {

            AuthMiddleWare.requirePermission(PermissionConstant.CREATE_USER).handle(ctx);

//                 var body =  ctx.bodyValidator(Map.class)
//                   .check(b->b.containsKey("name"),"Field name is required !")
//                   .check(b->b.get("name") instanceof String,"Name must be String !")
//                   .check(b->b.containsKey("department"),"Field department is required !")
//                   .check(b->b.get("department") instanceof Integer,"Department must be Number !")
//                         .get();
//           
//        String name =(String) body.get("name");
//         int department =(int) body.get("department");
//         
//         String email = (String) body.get("email");
//         String password=(String) body.get("password");
//         
//        List<Integer> permissions = (List<Integer>) body.get("permissions");
            String name = ctx.formParam("name");
            int department = Integer.parseInt(ctx.formParam("department"));
            String email = ctx.formParam("email");
            String password = ctx.formParam("password");

            List<Integer> permissions = ctx.formParams("permissions").stream()
                    .map(Integer::parseInt).toList();

            UploadedFile file = ctx.uploadedFile("image");
            String fileName = null;

            if (file == null) {
                return;
            }

            String contentType = file.contentType();
            if (!List.of("image/jpg", "image/png", "image/jpeg").contains(contentType)) {

                ctx.status(400).json(Map.of("error", "Invalid Image type"));
                return;
            }

            if (file.size() > 10 * 1024 * 1024) {

                ctx.status(400).json(Map.of("error", "Maximum file size is 10 MB"));
                return;
            }

//            fileName =UUID.randomUUID()+"_"+file.filename();
//          now uploading to cloudinary
            String profileImageUrl = "";
            try (InputStream is = file.content(); ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {

                byte[] data = new byte[1024];
                int nRead;
                while ((nRead = is.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, nRead);
                }
                buffer.flush();

                Map uploadResult = CloudinaryConfig.cloudinary.uploader().upload(
                        buffer.toByteArray(),
                        ObjectUtils.asMap("folder", "profile_images", "resource_type", "image")
                );

                profileImageUrl = (String) uploadResult.get("secure_url");
            }

            UserResponse userResponse = userService.createUser(new UserRequest(name, email, password, department, permissions, profileImageUrl));

            //for file storage
//            Path path = Paths.get("uploads/" + fileName);
//
//            Files.createDirectories(path.getParent());
//
//            try (InputStream is = file.content()) {
//                Files.copy(is, path, StandardCopyOption.REPLACE_EXISTING);
//            }
            ctx.status(201).json(userResponse);
        });

        app.put("/user", ctx -> {

            AuthMiddleWare.requirePermission(PermissionConstant.UPDATE_USER).handle(ctx);

            String name = ctx.formParam("name");
            int department = Integer.parseInt(ctx.formParam("department"));
            String email = ctx.formParam("email");
            String password = ctx.formParam("password");

            List<Integer> permissions = ctx.formParams("permissions").stream()
                    .map(Integer::parseInt).toList();

            UserRequest userRequest = new UserRequest(name, email, password, department, permissions, null);

            ctx.status(200).json(userService.updateUser(userRequest, Integer.parseInt(ctx.queryParam("user_id"))));

        });

        app.delete("/user/{id}", ctx -> {

            AuthMiddleWare.requirePermission(PermissionConstant.DELETE_USER).handle(ctx);

            int user_id = Integer.parseInt(ctx.pathParam("id"));
            ctx.status(200).result(userService.deleteUser(user_id).toString());
        });

        //refresh token
        app.post("/refresh", ctx -> {

            String token = ctx.bodyValidator(Map.class)
                    .check(b -> b.containsKey("refreshToken"), "refreshToken required")
                    .get()
                    .get("refreshToken").toString();

            try {

                var claims = JwtUtil.validateToken(token);

                var newClaims = Map.of(
                        "email", claims.get("email"),
                        "permissions", claims.get("permissions")
                );

                String newToken = JwtUtil.generateAcessToken(claims.getSubject(), claims);

                ctx.json(Map.of("accessToken", newToken)).status(200);

            } catch (Exception e) {

                throw new UnauthorizedException("Invalid refreshToken", 401);
            }

        });

        app.get("/dashboard", ctx -> {

            AuthMiddleWare.requirePermission(PermissionConstant.READ_DASHBOARD).handle(ctx);
            ctx.json(userService.getDashboardDetails()).status(200);
        });

        app.post("/forget-password", ctx -> {

            SendForgetPasswordEmail request = ctx.bodyAsClass(SendForgetPasswordEmail.class);

            ctx.json(userService.sendForgetPasswordEmail(request)).status(200);
        });

        app.post("/reset-password", ctx -> {
            ForgetPasswordRequest request = ctx.bodyAsClass(ForgetPasswordRequest.class);
            ctx.json(userService.resetPassword(request)).status(200);
        });

        app.post("/user/changePassword", ctx -> {

            ChangePasswordRequest request = ctx.bodyAsClass(ChangePasswordRequest.class);

            ctx.json(userService.changePassword(request)).status(200);

        });
    }

}
