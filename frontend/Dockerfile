# Build Stage
FROM node:14-alpine as build

WORKDIR /code

COPY package.json package.json
COPY package-lock.json package-lock.json

# Install dependencies
RUN npm install

# Copy the rest of the application code
COPY . .

# Fix esbuild for cross-platform compatibility
RUN npm install --platform=linux-x64 esbuild@latest

# Build the application
RUN npm run build

# Production Stage
FROM nginx:1.22-alpine as prod

# Copy built artifacts from the build stage
COPY --from=build /code/dist /usr/share/nginx/html

# Expose port 80
EXPOSE 3000

# Default command to start Nginx
CMD ["nginx", "-g", "daemon off;"]
