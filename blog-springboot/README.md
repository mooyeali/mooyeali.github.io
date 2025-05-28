# Mooyeali Code Den - Spring Boot Blog Website

A modern blog website built with Spring Boot, converted from a static HTML blog to a dynamic server-side application.

## Features

- **Homepage**: Displays latest blog posts with pagination
- **Post Viewing**: Individual post pages with view count tracking
- **Search**: Full-text search functionality across blog posts
- **Admin Panel**: Content management interface for creating and editing posts
- **Responsive Design**: Mobile-friendly layout with original CSS styling
- **Chinese Content**: Programming-focused blog posts in Chinese

## Technology Stack

- **Backend**: Spring Boot 3.1.0
- **Template Engine**: Thymeleaf
- **Database**: H2 (in-memory for development)
- **ORM**: Spring Data JPA
- **Build Tool**: Maven
- **Java Version**: 17

## Project Structure

```
src/
├── main/
│   ├── java/com/mooyeali/blog/
│   │   ├── BlogApplication.java          # Main application class
│   │   ├── config/
│   │   │   └── DataInitializer.java      # Sample data initialization
│   │   ├── controller/
│   │   │   └── BlogController.java       # Web controllers
│   │   ├── model/
│   │   │   └── BlogPost.java             # Entity model
│   │   ├── repository/
│   │   │   └── BlogPostRepository.java   # Data access layer
│   │   └── service/
│   │       └── BlogPostService.java      # Business logic
│   └── resources/
│       ├── application.yml               # Configuration
│       ├── static/css/                   # CSS files
│       └── templates/                    # Thymeleaf templates
```

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

### Running the Application

1. Clone the repository:
```bash
git clone <repository-url>
cd blog-springboot
```

2. Run the application:
```bash
mvn spring-boot:run
```

3. Access the application:
- Homepage: http://localhost:12000
- Admin Panel: http://localhost:12000/admin

## API Endpoints

- `GET /` - Homepage with blog posts
- `GET /post/{id}` - View individual blog post
- `GET /search` - Search blog posts
- `GET /admin` - Admin dashboard
- `GET /admin/new` - Create new post form
- `POST /admin/save` - Save blog post
- `GET /admin/edit/{id}` - Edit post form
- `POST /admin/delete/{id}` - Delete blog post

## Database Schema

### BlogPost Entity
- `id` (Long) - Primary key
- `title` (String) - Post title
- `summary` (String) - Post summary
- `content` (Text) - Post content
- `createdAt` (LocalDateTime) - Creation timestamp
- `updatedAt` (LocalDateTime) - Last update timestamp
- `viewCount` (Long) - View counter
- `isPublished` (Boolean) - Publication status

## Sample Data

The application comes with 5 sample blog posts covering:
1. Java Swing text matcher development experience
2. Java IO streams fundamentals
3. Java NIO and Buffer concepts
4. Linux kill command best practices
5. LeetCode string formatting problem solution

## Configuration

Key configuration in `application.yml`:
- Server port: 12000
- H2 database with web console enabled
- JPA with automatic DDL generation
- Thymeleaf template caching disabled for development

## Development Notes

- Uses H2 in-memory database (data resets on restart)
- Sample data is automatically loaded on startup
- Templates support Chinese characters with UTF-8 encoding
- CSS styling preserved from original static blog
- View counts are tracked and displayed

## Future Enhancements

- User authentication and authorization
- Comment system
- Tag/category management
- Rich text editor for admin panel
- File upload for images
- RSS feed generation
- SEO optimization

## License

This project is open source and available under the MIT License.