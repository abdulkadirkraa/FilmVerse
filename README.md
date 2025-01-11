# 🎨 Android Compose Bootcamp Final Project

## 🚀 Project Overview

This project was developed as the final assignment for the **Android Compose Bootcamp** sponsored by **FMSS** and **TechCareer**. It showcases a robust **multi-module Clean Architecture** with the **MVVM (Model-View-ViewModel)** design pattern for scalable and maintainable development.

---

# 🖼️ Project Screenshots

<table>
  <tr>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/8ca85af2-175b-48ff-811f-91b5ea828b0d" alt="ScreenHome" width="200"/>
      <br/>
      <b>ScreenHome</b>
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/968ea8cb-7399-48e9-afdd-58fbfec2cfe6" alt="ScreenHome with badges" width="200"/>
      <br/>
      <b>ScreenHome with badges</b>
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/50649172-c03a-4887-9c19-71dee09b136a" alt="ScreenHome bottomsheet" width="200"/>
      <br/>
      <b>ScreenHome bottomsheet</b>
    </td>
  </tr>
  <tr>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/ee3a1b7d-c8d1-417b-92b7-de738f7949c1" alt="ScreenDetail" width="200"/>
      <br/>
      <b>ScreenDetail</b>
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/69d65158-f4ff-4851-8e2a-91f317db2a6d" alt="ScreenCard" width="200"/>
      <br/>
      <b>ScreenCard</b>
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/fd0138ff-6f5e-4e3d-9b27-e9978c1bd48a" alt="ScreenFavorites" width="200"/>
      <br/>
      <b>ScreenFavorites</b>
    </td>
  </tr>
  <tr>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/01daea90-c7ce-4930-b109-8fcd25f20d3f" alt="ScreenFavorites" width="200"/>
      <br/>
      <b>Error Component</b>
    </td>
  </tr>
</table>

## 🛠️ Technologies and Libraries Used

| **Technology**       | **Purpose**                                  |
|-----------------------|----------------------------------------------|
| **Jetpack Compose**   | Declarative UI components                    |
| **Retrofit**          | API requests and networking                  |
| **Room**              | Local database storage                       |
| **DataStore**         | Key-value storage                            |
| **Hilt**              | Dependency Injection                         |
| **Navigation**        | Screen navigation                            |
| **Gson**              | JSON serialization/deserialization           |
| **Coroutines & Flow** | Asynchronous programming & reactive streams  |
| **Coil**              | Image loading                                |
| **Lottie**            | Animations                                   |
| **Compose Animation** | Shared element transitions                   |
| **LiveData**          | State management                             |
| **Pager**             | Horizontal paginated content display         |

---

## 🏗️ Project Architecture

The project follows **Clean Architecture** principles to ensure separation of concerns and maintainability:

### 📂 Modules

- **App Module**: Contains the Presentation Layer.
- **Data Module**: Manages data sources (local and remote).
- **Domain Module**: Handles business logic.
- **Common Module**: Provides shared utilities and constants.
