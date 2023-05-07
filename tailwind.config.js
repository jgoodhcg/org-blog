/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./**/*.{html,js}"],
  theme: {
    colors: {
      transparent: 'transparent',
      current: 'currentColor',
      black: '#000000',
      white: '#ffffff',
      gray: {
        100: '#D7D7D7',
        DEFAULT: '#C1C1C1',
        900: '#ABABAB',
      },
      yellow: {
        100: "#FDCF70",
        200: "#F9BF46",
        DEFAULT: "#EDAD28",
        400: "#B8820F",
        900: "#865B00",
      },
      orange: {
        100: "#F67F4F",
        DEFAULT: "#DC4C19",
        900: "#A23512",
      },
      red: {
        100: "#EF5252",
        DEFAULT: "#D11414",
        900: "#9C1010",
      },
      blue: {
        100: "#7FA1C1",
        DEFAULT: "#205283",
        900: "#05203F",
      },
      'light-blue': {
        100: "#B4D9F4",
        DEFAULT: "#5BA7DF",
        900: "#2D76AE",
      },
      green: {
        100: "#7AF49B",
        DEFAULT: "#1EBF4B",
        900: "#206D34",
      },
      purple: {
        100: "#DF97F5",
        DEFAULT: "#BC5BE6",
        900: "#77508D",
      },
    },
    extend: {},
  },
  plugins: [],
}
