# Conectar Firebase (login real + progreso en la nube)

La app funciona SIN esto (usa cuentas locales en el teléfono). Para pasar a la nube:

1. Entra a https://console.firebase.google.com -> "Agregar proyecto" (nombre libre, Analytics opcional).
2. En el proyecto: "Agregar app" -> Android.
   - Nombre del paquete: com.example.englishapp
   - Descarga google-services.json y ponlo en: EnglishApp/app/google-services.json
3. Build -> Authentication -> "Comenzar" -> Sign-in method -> habilita "Correo electrónico/contraseña".
4. Build -> Firestore Database -> "Crear base de datos" (modo producción, región cercana).
   Pestaña "Reglas" -> pega esto y publica:

   rules_version = '2';
   service cloud.firestore {
     match /databases/{database}/documents {
       match /users/{uid}/{document=**} {
         allow read, write: if request.auth != null && request.auth.uid == uid;
       }
     }
   }

5. En Android Studio: File -> Sync Project with Gradle Files -> Run.
   En la pestaña Perfil debe decir "Cuenta en la nube (Firebase)".

Estructura que se guarda:
  Authentication: usuario (correo + contraseña, con hash en servidores de Google)
  Firestore: users/{uid} -> { name, email }
             users/{uid}/progress/{1..5} -> { best, last, attempts }
