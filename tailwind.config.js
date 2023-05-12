/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./**/*.{html,js}"],
  theme: {
    colors: {
      transparent: 'transparent',
      current: 'currentColor',
      black: '#000000',
      white: '#ffffff',
      yellow: {
        100: "#FDCF70",
        200: "#F9BF46",
        DEFAULT: "#EDAD28",
        400: "#B8820F",
        900: "#865B00",
      },
      pink: {
        100: '#FF88D1',
        DEFAULT: '#FF71CE',
        900: '#E064B7',
      },
      cyan: {
        100: '#33E1FD',
        DEFAULT: '#01CDFE',
        900: '#01B2D6',
      },
      green: {
        100: '#30FFB3',
        DEFAULT: '#05FFA1',
        900: '#05D68E',
      },
      purple: {
        100: '#CA7FFF',
        DEFAULT: '#B967FF',
        900: '#A355E2',
      },
      red: {
        100: '#FF8B8B',
        DEFAULT: '#FF6B6B',
        900: '#E25B5B',
      },
    },
    extend: {},
  },
  plugins: [],
}
