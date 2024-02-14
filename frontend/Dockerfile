FROM node:16

WORKDIR /app

COPY package*.json ./
RUN npm install

COPY public ./public
COPY src ./src
COPY index.html ./
COPY vite.config.js ./

EXPOSE 5173

CMD ["npx", "vite"]
