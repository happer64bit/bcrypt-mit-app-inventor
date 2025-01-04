# Use an official OpenJDK 8 runtime as the base image
FROM openjdk:8-jdk

# Set the working directory inside the container
WORKDIR /app

# Install Node.js and curl (needed for rush-cli)
RUN apt-get update && apt-get install -y \
    curl \
    gnupg \
    && curl -fsSL https://deb.nodesource.com/setup_18.x | bash - \
    && apt-get install -y nodejs \
    && rm -rf /var/lib/apt/lists/*

# Install rush-cli using the provided script
RUN curl https://raw.githubusercontent.com/shreyashsaitwal/rush-cli/main/scripts/install/install.sh -fsSL | sh

# Copy the current folder's contents into the container
COPY . .

# Install dependencies using rush
RUN rush install

# Default command to run `rush build`
CMD ["rush", "build"]
